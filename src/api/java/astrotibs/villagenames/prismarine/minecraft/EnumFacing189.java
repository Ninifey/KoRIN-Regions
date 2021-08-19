package astrotibs.villagenames.prismarine.minecraft;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

import net.minecraft.util.MathHelper;

public enum EnumFacing189 implements IStringSerializable
{
    DOWN(0, 1, -1, "down", EnumFacing189.AxisDirection.NEGATIVE, EnumFacing189.Axis.Y, new Vec3i(0, -1, 0)),
    UP(1, 0, -1, "up", EnumFacing189.AxisDirection.POSITIVE, EnumFacing189.Axis.Y, new Vec3i(0, 1, 0)),
    NORTH(2, 3, 2, "north", EnumFacing189.AxisDirection.NEGATIVE, EnumFacing189.Axis.Z, new Vec3i(0, 0, -1)),
    SOUTH(3, 2, 0, "south", EnumFacing189.AxisDirection.POSITIVE, EnumFacing189.Axis.Z, new Vec3i(0, 0, 1)),
    WEST(4, 5, 1, "west", EnumFacing189.AxisDirection.NEGATIVE, EnumFacing189.Axis.X, new Vec3i(-1, 0, 0)),
    EAST(5, 4, 3, "east", EnumFacing189.AxisDirection.POSITIVE, EnumFacing189.Axis.X, new Vec3i(1, 0, 0));

    /** Ordering index for D-U-N-S-W-E */
    private final int index;
    /** Index of the opposite Facing in the VALUES array */
    private final int opposite;
    /** Ordering index for the HORIZONTALS field (S-W-N-E) */
    private final int horizontalIndex;
    private final String name;
    private final EnumFacing189.Axis axis;
    private final EnumFacing189.AxisDirection axisDirection;
    /** Normalized Vector that points in the direction of this Facing */
    private final Vec3i directionVec;
    /** All facings in D-U-N-S-W-E order */
    public static final EnumFacing189[] VALUES = new EnumFacing189[6];
    /** All Facings with horizontal axis in order S-W-N-E */
    public static final EnumFacing189[] HORIZONTALS = new EnumFacing189[4];
    private static final Map<String, EnumFacing189> NAME_LOOKUP = Maps.<String, EnumFacing189>newHashMap();

    private EnumFacing189(int indexIn, int oppositeIn, int horizontalIndexIn, String nameIn, EnumFacing189.AxisDirection axisDirectionIn, EnumFacing189.Axis axisIn, Vec3i directionVecIn)
    {
        this.index = indexIn;
        this.horizontalIndex = horizontalIndexIn;
        this.opposite = oppositeIn;
        this.name = nameIn;
        this.axis = axisIn;
        this.axisDirection = axisDirectionIn;
        this.directionVec = directionVecIn;
    }

    /**
     * Get the Index of this Facing (0-5). The order is D-U-N-S-W-E
     */
    public int getIndex()
    {
        return this.index;
    }

    /**
     * Get the index of this horizontal facing (0-3). The order is S-W-N-E
     */
    public int getHorizontalIndex()
    {
        return this.horizontalIndex;
    }

    /**
     * Get the AxisDirection of this Facing.
     */
    public EnumFacing189.AxisDirection getAxisDirection()
    {
        return this.axisDirection;
    }

    /**
     * Get the opposite Facing (e.g. DOWN => UP)
     */
    public EnumFacing189 getOpposite()
    {
        /**
         * Get a Facing by it's index (0-5). The order is D-U-N-S-W-E. Named getFront for legacy reasons.
         */
        return getFront(this.opposite);
    }

    /**
     * Rotate this Facing around the given axis clockwise. If this facing cannot be rotated around the given axis,
     * returns this facing without rotating.
     */
    public EnumFacing189 rotateAround(EnumFacing189.Axis axis)
    {
        switch (axis)
        {
            case X:

                if (this != WEST && this != EAST)
                {
                    return this.rotateX();
                }

                return this;
            case Y:

                if (this != UP && this != DOWN)
                {
                    return this.rotateY();
                }

                return this;
            case Z:

                if (this != NORTH && this != SOUTH)
                {
                    return this.rotateZ();
                }

                return this;
            default:
                throw new IllegalStateException("Unable to get CW facing for axis " + axis);
        }
    }

    /**
     * Rotate this Facing around the Y axis clockwise (NORTH => EAST => SOUTH => WEST => NORTH)
     */
    public EnumFacing189 rotateY()
    {
        switch (this)
        {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            default:
                throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        }
    }

    /**
     * Rotate this Facing around the X axis (NORTH => DOWN => SOUTH => UP => NORTH)
     */
    private EnumFacing189 rotateX()
    {
        switch (this)
        {
            case NORTH:
                return DOWN;
            case EAST:
            case WEST:
            default:
                throw new IllegalStateException("Unable to get X-rotated facing of " + this);
            case SOUTH:
                return UP;
            case UP:
                return NORTH;
            case DOWN:
                return SOUTH;
        }
    }

    /**
     * Rotate this Facing around the Z axis (EAST => DOWN => WEST => UP => EAST)
     */
    private EnumFacing189 rotateZ()
    {
        switch (this)
        {
            case EAST:
                return DOWN;
            case SOUTH:
            default:
                throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
            case WEST:
                return UP;
            case UP:
                return EAST;
            case DOWN:
                return WEST;
        }
    }

    /**
     * Rotate this Facing around the Y axis counter-clockwise (NORTH => WEST => SOUTH => EAST => NORTH)
     */
    public EnumFacing189 rotateYCCW()
    {
        switch (this)
        {
            case NORTH:
                return WEST;
            case EAST:
                return NORTH;
            case SOUTH:
                return EAST;
            case WEST:
                return SOUTH;
            default:
                throw new IllegalStateException("Unable to get CCW facing of " + this);
        }
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetX()
    {
        return this.axis == EnumFacing189.Axis.X ? this.axisDirection.getOffset() : 0;
    }

    public int getFrontOffsetY()
    {
        return this.axis == EnumFacing189.Axis.Y ? this.axisDirection.getOffset() : 0;
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetZ()
    {
        return this.axis == EnumFacing189.Axis.Z ? this.axisDirection.getOffset() : 0;
    }

    /**
     * Same as getName, but does not override the method from Enum.
     */
    public String getName2()
    {
        return this.name;
    }

    public EnumFacing189.Axis getAxis()
    {
        return this.axis;
    }

    /**
     * Get the facing specified by the given name
     */
    public static EnumFacing189 byName(String name)
    {
        return name == null ? null : (EnumFacing189)NAME_LOOKUP.get(name.toLowerCase());
    }

    /**
     * Get a Facing by it's index (0-5). The order is D-U-N-S-W-E. Named getFront for legacy reasons.
     */
    public static EnumFacing189 getFront(int index)
    {
        return VALUES[MathHelper.abs_int(index % VALUES.length)];
    }

    /**
     * Get a Facing by it's horizontal index (0-3). The order is S-W-N-E.
     */
    public static EnumFacing189 getHorizontal(int p_176731_0_)
    {
        return HORIZONTALS[MathHelper.abs_int(p_176731_0_ % HORIZONTALS.length)];
    }

    /**
     * Get the Facing corresponding to the given angle (0-360). An angle of 0 is SOUTH, an angle of 90 would be WEST.
     */
    public static EnumFacing189 fromAngle(double angle)
    {
        /**
         * Get a Facing by it's horizontal index (0-3). The order is S-W-N-E.
         */
        return getHorizontal(MathHelper.floor_double(angle / 90.0D + 0.5D) & 3);
    }

    /**
     * Choose a random Facing using the given Random
     */
    public static EnumFacing189 random(Random rand)
    {
        return values()[rand.nextInt(values().length)];
    }

    public static EnumFacing189 getFacingFromVector(float p_176737_0_, float p_176737_1_, float p_176737_2_)
    {
        EnumFacing189 enumfacing = NORTH;
        float f = Float.MIN_VALUE;

        for (EnumFacing189 enumfacing1 : values())
        {
            float f1 = p_176737_0_ * enumfacing1.directionVec.getX() + p_176737_1_ * enumfacing1.directionVec.getY() + p_176737_2_ * enumfacing1.directionVec.getZ();

            if (f1 > f)
            {
                f = f1;
                enumfacing = enumfacing1;
            }
        }

        return enumfacing;
    }

    @Override
	public String toString()
    {
        return this.name;
    }

    @Override
	public String getName()
    {
        return this.name;
    }

    public static EnumFacing189 func_181076_a(EnumFacing189.AxisDirection p_181076_0_, EnumFacing189.Axis p_181076_1_)
    {
        for (EnumFacing189 enumfacing : values())
        {
            if (enumfacing.getAxisDirection() == p_181076_0_ && enumfacing.getAxis() == p_181076_1_)
            {
                return enumfacing;
            }
        }

        throw new IllegalArgumentException("No such direction: " + p_181076_0_ + " " + p_181076_1_);
    }

    /**
     * Get a normalized Vector that points in the direction of this Facing.
     */
    public Vec3i getDirectionVec()
    {
        return this.directionVec;
    }

    static
    {
        for (EnumFacing189 enumfacing : values())
        {
            VALUES[enumfacing.index] = enumfacing;

            if (enumfacing.getAxis().isHorizontal())
            {
                HORIZONTALS[enumfacing.horizontalIndex] = enumfacing;
            }

            NAME_LOOKUP.put(enumfacing.getName2().toLowerCase(), enumfacing);
        }
    }

    public static enum Axis implements Predicate<EnumFacing189>, IStringSerializable {
        X("x", EnumFacing189.Plane.HORIZONTAL),
        Y("y", EnumFacing189.Plane.VERTICAL),
        Z("z", EnumFacing189.Plane.HORIZONTAL);

        private static final Map<String, EnumFacing189.Axis> NAME_LOOKUP = Maps.<String, EnumFacing189.Axis>newHashMap();
        private final String name;
        private final EnumFacing189.Plane plane;

        private Axis(String name, EnumFacing189.Plane plane)
        {
            this.name = name;
            this.plane = plane;
        }

        /**
         * Get the axis specified by the given name
         */
        public static EnumFacing189.Axis byName(String name)
        {
            return name == null ? null : (EnumFacing189.Axis)NAME_LOOKUP.get(name.toLowerCase());
        }

        /**
         * Like getName but doesn't override the method from Enum.
         */
        public String getName2()
        {
            return this.name;
        }

        /**
         * If this Axis is on the vertical plane (Only true for Y)
         */
        public boolean isVertical()
        {
            return this.plane == EnumFacing189.Plane.VERTICAL;
        }

        /**
         * If this Axis is on the horizontal plane (true for X and Z)
         */
        public boolean isHorizontal()
        {
            return this.plane == EnumFacing189.Plane.HORIZONTAL;
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        @Override
		public boolean apply(EnumFacing189 p_apply_1_)
        {
            return p_apply_1_ != null && p_apply_1_.getAxis() == this;
        }

        /**
         * Get this Axis' Plane (VERTICAL for Y, HORIZONTAL for X and Z)
         */
        public EnumFacing189.Plane getPlane()
        {
            return this.plane;
        }

        @Override
		public String getName()
        {
            return this.name;
        }

        static
        {
            for (EnumFacing189.Axis enumfacing$axis : values())
            {
                NAME_LOOKUP.put(enumfacing$axis.getName2().toLowerCase(), enumfacing$axis);
            }
        }
    }

    public static enum AxisDirection {
        POSITIVE(1, "Towards positive"),
        NEGATIVE(-1, "Towards negative");

        private final int offset;
        private final String description;

        private AxisDirection(int offset, String description)
        {
            this.offset = offset;
            this.description = description;
        }

        /**
         * Get the offset for this AxisDirection. 1 for POSITIVE, -1 for NEGATIVE
         */
        public int getOffset()
        {
            return this.offset;
        }

        @Override
		public String toString()
        {
            return this.description;
        }
    }

    public static enum Plane implements Predicate<EnumFacing189>, Iterable<EnumFacing189> {
        HORIZONTAL,
        VERTICAL;

        /**
         * All EnumFacing189 values for this Plane
         */
        public EnumFacing189[] facings()
        {
            switch (this)
            {
                case HORIZONTAL:
                    return new EnumFacing189[] {EnumFacing189.NORTH, EnumFacing189.EAST, EnumFacing189.SOUTH, EnumFacing189.WEST};
                case VERTICAL:
                    return new EnumFacing189[] {EnumFacing189.UP, EnumFacing189.DOWN};
                default:
                    throw new Error("Someone\'s been tampering with the universe!");
            }
        }

        /**
         * Choose a random Facing from this Plane using the given Random
         */
        public EnumFacing189 random(Random rand)
        {
            EnumFacing189[] aenumfacing = this.facings();
            return aenumfacing[rand.nextInt(aenumfacing.length)];
        }

        @Override
		public boolean apply(EnumFacing189 p_apply_1_)
        {
            return p_apply_1_ != null && p_apply_1_.getAxis().getPlane() == this;
        }

        @Override
		public Iterator<EnumFacing189> iterator()
        {
            return Iterators.<EnumFacing189>forArray(this.facings());
        }
    }
}